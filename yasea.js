'use strict';

import React, {Component, PropTypes} from 'react';
import {
    NativeAppEventEmitter,
    NativeModules,
    Platform,
    StyleSheet,
    requireNativeComponent,
    DeviceEventEmitter,
    View
} from 'react-native';

var RNYaseaModule = NativeModules.RNYaseaModule;

class YaseaView extends Component {

    constructor(props) {
        super(props);
        this.listeners = [];
    }

    componentWillMount() {
        let that = this;
        this.listeners.push(NativeAppEventEmitter.addListener('publish',
            (message) => {
                that.props.onPublish(message);
            }
        ));

        this.listeners.push(NativeAppEventEmitter.addListener('record',
            (message) => {
                that.props.onRecord(message);
            }
        ));

        this.listeners.push(NativeAppEventEmitter.addListener('network',
            (message) => {
                that.props.onNetwork(message);
            }
        ));
    }

    componentWillUnmount() {
        this.listeners.forEach(sub => sub.remove());
    }

    render() {
        return <NativeRNYaseaView {...this.props}/>;
    }

    startPublish(streamUrl, streamkey) {
        let url = streamUrl + '/' + streamkey;
        RNYaseaModule.startPublish(url);
    }

    stopPublish() {
        RNYaseaModule.stopPublish();
    }

    setOutputResolution(width, height) {
        RNYaseaModule.setOutputResolution(width, height);
    }
}

YaseaView.defaultProps = {
    onPublish: () => {
    },
    onRecord: () => {
    },
    onNetwork: () => {
    }
};

YaseaView.propTypes = {
    ...View.propTypes,
    onPublish: PropTypes.func,
    onRecord: PropTypes.func,
    onNetwork: PropTypes.func
};

var NativeRNYaseaView = requireNativeComponent('RNYaseaView', YaseaView);

module.exports = YaseaView;